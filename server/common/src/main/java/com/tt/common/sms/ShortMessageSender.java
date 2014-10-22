package com.tt.common.sms;

import java.util.Map;

public interface ShortMessageSender {

	void send(Object templateId, String[] receivers, Map<String, Object> variables) throws Exception;
}
