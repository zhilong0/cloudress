package com.df.spec.locality.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.df.spec.locality.dao.RegionDao;
import com.df.spec.locality.exception.DuplicateRegionException;
import com.df.spec.locality.exception.RegionErrorCode;
import com.df.spec.locality.exception.RegionWithCodeNotFoundException;
import com.df.spec.locality.exception.SpecialityBaseException;
import com.df.spec.locality.geo.Coordinate;
import com.df.spec.locality.geo.GeoService;
import com.df.spec.locality.model.Region;
import com.df.spec.locality.service.RegionService;
import com.googlecode.jcsv.CSVStrategy;
import com.googlecode.jcsv.reader.CSVReader;
import com.googlecode.jcsv.reader.internal.CSVReaderBuilder;
import com.googlecode.jcsv.reader.internal.DefaultCSVEntryParser;

public class RegionServiceImpl implements RegionService {

	private RegionDao regionDao;

	private GeoService geoService;

	private static final Logger logger = LoggerFactory.getLogger(RegionServiceImpl.class);

	public RegionServiceImpl(RegionDao regionDao, GeoService geoService) {
		this.regionDao = regionDao;
		this.geoService = geoService;
	}

	public void setRegionDao(RegionDao regionDao) {
		this.regionDao = regionDao;
	}

	public void setGeoService(GeoService geoService) {
		this.geoService = geoService;
	}

	@Override
	public void addRegion(Region region) {
		regionDao.addRegion(region);
	}

	@Override
	public List<Region> getRegionList() {
		return regionDao.getRegionList();
	}

	@Override
	public Region getRegionByCode(String regionCode, boolean throwException) {
		Region found = regionDao.getRegionByCode(regionCode);
		if (found == null && throwException) {
			throw new RegionWithCodeNotFoundException(null, regionCode);
		} else {
			return found;
		}
	}

	@Override
	public Region getRegionByCoordinate(Coordinate coordinate) {
		Region region = geoService.lookupRegionWithCoordiate(coordinate);
		return regionDao.findRegion(region);
	}

	@Override
	public void importFromCSV(InputStream in, boolean continueOnError) {
		Assert.notNull(in);
		Reader reader = new InputStreamReader(in);
		CSVReaderBuilder<String[]> builder = new CSVReaderBuilder<String[]>(reader).strategy(CSVStrategy.UK_DEFAULT);
		CSVReader<String[]> csvReader = builder.entryParser(new DefaultCSVEntryParser()).build();
		List<String[]> entries;
		try {
			csvReader.readHeader();
			entries = csvReader.readAll();
		} catch (IOException ex) {
			throw new SpecialityBaseException(ex, RegionErrorCode.REGION_CSV_READ_ERROR);
		}
		for (String[] entry : entries) {
			if (entry.length < 2) {
				if (continueOnError) {
					continue;
				} else {
					SpecialityBaseException ex = new SpecialityBaseException(null, "Invalid region entry, province and city must be provided");
					ex.setErrorCode(RegionErrorCode.REGION_CSV_ENTRY_ERROR);
					throw ex;

				}
			}
			Region region = new Region(entry[0], entry[1]);
			if (entry.length >= 3) {
				region.setDistrict(entry[2]);
			}
			try {
				this.addRegion(region);
			} catch (DuplicateRegionException ex) {
				if (continueOnError) {
					logger.warn("Region %s already exist", ex.getRegion());
					continue;
				} else {
					throw ex;
				}
			}
		}
	}

	@Override
	public Region findRegion(String province, String city, String district) {
		Assert.notNull(province);
		Assert.notNull(city);
		return regionDao.findRegion(new Region(province, city, district));
	}
}
