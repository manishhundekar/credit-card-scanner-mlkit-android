package com.manish.creditcardscanner.charting.interfaces.dataprovider;

import com.manish.creditcardscanner.charting.components.YAxis.AxisDependency;
import com.manish.creditcardscanner.charting.data.BarLineScatterCandleBubbleData;
import com.manish.creditcardscanner.charting.utils.Transformer;

public interface BarLineScatterCandleBubbleDataProvider extends ChartInterface {

    Transformer getTransformer(AxisDependency axis);
    boolean isInverted(AxisDependency axis);
    
    float getLowestVisibleX();
    float getHighestVisibleX();

    BarLineScatterCandleBubbleData getData();
}
