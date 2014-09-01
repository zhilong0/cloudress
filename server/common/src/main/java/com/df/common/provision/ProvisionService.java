package com.df.common.provision;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import com.df.common.provision.ProvisionContext.ProvisioningStatus;

public class ProvisionService implements BeanPostProcessor, ApplicationListener<ContextRefreshedEvent> {

	private Map<String, List<ImporterBean>> beans = new HashMap<String, List<ImporterBean>>();

	private static final Logger logger = LoggerFactory.getLogger(ProvisionService.class);

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		if (bean instanceof ImporterBean) {
			String groupName = ((ImporterBean) bean).getGroupName();
			List<ImporterBean> groupBeans = beans.get(groupName);
			if (groupBeans == null) {
				groupBeans = new ArrayList<ImporterBean>();
				beans.put(groupName, groupBeans);
			}
			groupBeans.add((ImporterBean) bean);
		}
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (beans.size() == 0) {
			logger.info("No importer bean is configured");
			return;
		}

		ProvisionContext context = new ProvisionContext();
		for (String group : beans.keySet()) {
			List<ImporterBean> groupBeans = beans.get(group);
			Collections.sort(beans.get(group), new Comparator<ImporterBean>() {

				@Override
				public int compare(ImporterBean obj1, ImporterBean obj2) {
					return obj1.getOrder() - obj2.getOrder();
				}
			});

			for (ImporterBean pb : groupBeans) {
				try {
					pb.execute(context);
				} catch (Exception ex) {
					context.addBeanRunningStatus(pb.getClass(), ex);
				}
			}
		}

		List<ProvisioningStatus> errorBeans = context.listBeansWithError();
		for (ProvisioningStatus errorBean : errorBeans) {
			String fmt = "Running importer bean %s with error %s";
			logger.error(String.format(fmt, errorBean.getBeanClassName(), errorBean.getError().getMessage()), errorBean.getError());
		}

		beans.clear();
	}
}
