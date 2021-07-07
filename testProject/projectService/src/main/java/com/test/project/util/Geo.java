package com.test.project.util;

public class Geo {
	
	private Country country;
	
	public static class Country{
		private String isoCode;

		public String getIsoCode() {
			return isoCode;
		}
		public void setIsoCode(String isoCode) {
			this.isoCode = isoCode;
		}
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}
}
