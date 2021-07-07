package com.test.project.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.maxmind.db.CHMCache;
import com.maxmind.db.Reader;

public final class GeoUtil {
	
	private static final String ISO_CODE_KR = "KR";
	
    static final Logger log = LoggerFactory.getLogger(GeoUtil.class);

	static GeoUtil geo;
	
	static GeoIpMatcher dmz_subnet = new GeoIpMatcher("10.1.0.0/16");
	static GeoIpMatcher was_subnet = new GeoIpMatcher("172.16.0.0/16");
	static GeoIpMatcher nas_subnet = new GeoIpMatcher("100.1.0.0/16");
	static GeoIpMatcher local = new GeoIpMatcher("127.0.0.1");
	
	static {
		try {
			geo = new GeoUtil();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	Reader reader;

	ObjectMapper mapper;
	
	
	public static final GeoUtil instance() {
		return geo;
	}

	GeoUtil() throws IOException {
		super();
		
		String mmdbpath = "/home/slpdev/infra-tools/data/geo/GeoLite2-Country/GeoLite2-Country.mmdb";
//		String INFRA_TOOLS_HOME = System.getenv("INFRA_TOOLS_HOME");
//		if(INFRA_TOOLS_HOME != null	&& "".equals(INFRA_TOOLS_HOME))
//			mmdbpath = String.format("%s/data/geo/GeoLite2-Country/GeoLite2-Country.mmdb", INFRA_TOOLS_HOME);
		log.info(mmdbpath);
		File database = new File(mmdbpath);
		if(!database.exists()) {
			log.error("", new FileNotFoundException(mmdbpath));
			return;
		}
		
        reader = new Reader(database, new CHMCache());
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.setSerializationInclusion(Include.NON_NULL);
        mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
	}

	public final boolean isKorea(String ip) throws IOException {
		return isCountry(ip, ISO_CODE_KR);
	}
	
	public final Geo findGeo(String ip) throws IOException {
		if(ignore(ip)) {
			System.out.println(ip + "ignore");
			return null;
		}
		
		if(reader == null)
			return null;
		
		InetAddress address = InetAddress.getByName(ip);
		JsonNode response = reader.get(address);
		if(response == null)
			return null;
		
		Geo geo = mapper.treeToValue(response, Geo.class);
		return geo;
	}

	public final boolean isCountry(String ip, String... isoCodes) throws IOException{
		Geo geo = findGeo(ip);
		if(geo == null)
			return true;
		
		String ipIsoCode = geo.getCountry().getIsoCode();
		
		for(String isoCode: isoCodes)
			if(isoCode.equals(ipIsoCode))
				return true;
		
		return false; 
	}
	
	boolean ignore(String ip) {
		return dmz_subnet.matches(ip)
				|| was_subnet.matches(ip)
				|| nas_subnet.matches(ip)
				|| local.matches(ip);
	}
}
