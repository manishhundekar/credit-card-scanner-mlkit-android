package com.manish.creditcardscanner.charting.interfaces.dataprovider;

import com.manish.creditcardscanner.charting.data.ScatterData;

public interface ScatterDataProvider extends BarLineScatterCandleBubbleDataProvider {

    ScatterData getScatterData();
}
