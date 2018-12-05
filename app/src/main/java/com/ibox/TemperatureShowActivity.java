package com.ibox;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.view.LineChartView;

import static com.ibox.R.id.temperature;

public class TemperatureShowActivity extends AppCompatActivity {
    private LineChartView lineChart;   //温度曲线
    private LineChartView lineChartH;         //
    String[] time = {"0时","4时","8时","12时","16时","20时","24时"};//X轴的标注
    int[] temperature = {30,32,34,40,36,34,32};//图表的数据
    int[] maxT={40,40,40,40,40,40,40};
    int[] minT={32,32,32,32,32,32,32};

    private List<PointValue> mPointValues = new ArrayList<PointValue>();
    private List<AxisValue> mAxisValues = new ArrayList<AxisValue>();
    private List<PointValue>  maxTValues=new ArrayList<PointValue>();
    private List<PointValue>  minTValues=new ArrayList<PointValue>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperature_show2);
        lineChart = (LineChartView)findViewById(R.id.line_chart);
        lineChartH=(LineChartView)findViewById(R.id.line_chart);
        getAxisLables();//获取x轴的标注
        getAxisPoints();//获取坐标点
        initLineChart();//初始化
        getMaxTPoints();//设置温度最高界限值
        getMinTPoints();//设置温度最低界限值
        new Thread(new ChangeRunnable()).start();
    }
    class ChangeRunnable implements Runnable
    {
        @Override
        public void run() {
            while(true) {
                for (int i = 0; i < temperature.length; i++) {
                    temperature[i]--;
                }
                mPointValues.clear();
                getAxisLables();//获取x轴的标注
                getAxisPoints();//获取坐标点
                initLineChart();//初始化
                getMaxTPoints();//设置温度最高界限值
                getMinTPoints();//设置温度最低界限值
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void initLineChart(){
        Line line = new Line(mPointValues).setColor(Color.BLACK).setCubic(false);  //折线的颜色
        Line maxTline=new Line(maxTValues).setColor(Color.RED).setCubic(false);
        Line minTline=new Line(minTValues).setColor(Color.RED).setCubic(false);
        List<Line> lines = new ArrayList<Line>();
        maxTline.setShape(ValueShape.CIRCLE);
        maxTline.setCubic(true);
        maxTline.setFilled(false);
        maxTline.setHasLabels(false);
        maxTline.setHasLines(true);
        maxTline.setHasPoints(false);

        minTline.setShape(ValueShape.CIRCLE);
        minTline.setCubic(true);
        minTline.setFilled(false);
        minTline.setHasLabels(false);
        minTline.setHasLines(true);
        minTline.setHasPoints(false);

        line.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.SQUARE）
        line.setCubic(true);//曲线是否平滑
        line.setFilled(true);//是否填充曲线的面积
        line.setHasLabels(true);//曲线的数据坐标是否加上备注
        //line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        line.setHasLines(true);//是否用直线显示。如果为false 则没有曲线只有点显示
        line.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示
        lines.add(line);
        lines.add(maxTline);
        lines.add(minTline);
        LineChartData data = new LineChartData();
        data.setLines(lines);



        //坐标轴
        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(true);
        axisX.setTextColor(Color.BLACK);  //设置字体颜色
        axisX.setName("");  //表格名称
        axisX.setTextSize(7);//设置字体大小
        axisX.setMaxLabelChars(7);  //最多几个X轴坐标
        axisX.setValues(mAxisValues);  //填充X轴的坐标名称

        data.setAxisXBottom(axisX); //x 轴在底部
//      data.setAxisXTop(axisX);  //x 轴在顶部

        Axis axisY = new Axis();  //Y轴
        axisY.setMaxLabelChars(7); //默认是3，只能看最后三个数字
        axisY.setTextColor(Color.BLACK);  //设置字体颜色
        axisY.setName("温度");//y轴标注
        axisY.setTextSize(7);//设置字体大小

        data.setAxisYLeft(axisY);  //Y轴设置在左边
//      data.setAxisYRight(axisY);  //y轴设置在右边

        //温度曲线设置行为属性，支持缩放、滑动以及平移
        lineChart.setInteractive(true);
        lineChart.setZoomType(ZoomType.HORIZONTAL_AND_VERTICAL);
        lineChart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        lineChart.setLineChartData(data);
        lineChart.setVisibility(View.VISIBLE);

        //湿度曲线
        lineChartH.setInteractive(true);
        lineChartH.setZoomType(ZoomType.HORIZONTAL_AND_VERTICAL);
        lineChartH.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        lineChartH.setLineChartData(data);
        lineChartH.setVisibility(View.VISIBLE);
    }
    /**
     * X 轴的显示
     */
    private void getAxisLables(){
        for (int i = 0; i < time.length; i++) {
            mAxisValues.add(new AxisValue(i).setLabel(time[i]));
        }
    }

    /**
     * 图表的每个点的显示
     */
    private void getAxisPoints(){
        for (int i = 0; i < temperature.length; i++) {
            mPointValues.add(new PointValue(i, temperature[i]));
        }
    }
    private void getMaxTPoints()
    {
        for(int i=0;i<maxT.length;i++)
        {
            maxTValues.add(new PointValue(i,maxT[i]));
        }
    }
    private void getMinTPoints()
    {
        for(int i=0;i<minT.length;i++)
        {
            minTValues.add(new PointValue(i,minT[i]));
        }
    }
}
