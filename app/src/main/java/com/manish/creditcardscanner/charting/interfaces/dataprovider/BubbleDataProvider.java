package com.manish.creditcardscanner.charting.interfaces.dataprovider;

import com.manish.creditcardscanner.charting.data.BubbleData;

public interface BubbleDataProvider extends BarLineScatterCandleBubbleDataProvider {

    BubbleData getBubbleData();
}
