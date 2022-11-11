package com.manish.creditcardscanner.charting.interfaces.dataprovider;

import com.manish.creditcardscanner.charting.data.CandleData;

public interface CandleDataProvider extends BarLineScatterCandleBubbleDataProvider {

    CandleData getCandleData();
}
