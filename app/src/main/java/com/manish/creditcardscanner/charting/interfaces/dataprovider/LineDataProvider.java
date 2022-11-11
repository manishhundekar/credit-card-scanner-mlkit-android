package com.manish.creditcardscanner.charting.interfaces.dataprovider;

import com.manish.creditcardscanner.charting.components.YAxis;
import com.manish.creditcardscanner.charting.data.LineData;

public interface LineDataProvider extends BarLineScatterCandleBubbleDataProvider {

    LineData getLineData();

    YAxis getAxis(YAxis.AxisDependency dependency);
}
