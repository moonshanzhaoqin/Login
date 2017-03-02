package com.yuyutechnology.exchange.manager;

import com.yuyutechnology.exchange.utils.oanda.OandaRespData;

public interface OandaRatesManager {

	public OandaRespData getCurrentPrices(String instruments);

}
