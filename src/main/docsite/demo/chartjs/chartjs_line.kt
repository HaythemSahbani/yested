package demo.chartjs

import net.yested.Div
import net.yested.div
import net.yested.Chart
import net.yested.LineChartData
import net.yested.LineChartSeries
import net.yested.bootstrap.btsButton
import net.yested.bootstrap.ButtonSize
import net.yested.bootstrap.ButtonLook
import net.yested.Colors
import net.yested.toHTMLColor

/**
 * Created by jean on 30.1.2015.
 */
fun createChartJSLineSection(): Div {

    val chart = Chart(width = 300, height = 250)

    val temperatureCZE = array(-2.81,-1.06,2.80,7.49,12.30,15.41,17.11,16.90,13.49,8.59,2.82,-1.06)
    val temperatureSVK = array(-2.03,0.85,5.44,10.72,15.49,18.52,20.11,19.70,16.13,10.81,4.89,0.11)

    val chartColorCZE = "rgba(151,187,205,1)"
    val chartColorSVK = "rgba(220,220,220,1)"

    val chartData =
        LineChartData(
            labels = array("Jan", "Feb", "Mar", "Apr", "May", "Jun", "July", "Aug", "Sept", "Oct", "Nov", "Dec"),
            datasets = array(
                        LineChartSeries(
                            label = "Czech Republic",
                            fillColor = chartColorCZE,
                            pointColor = chartColorCZE,
                            strokeColor = chartColorCZE,
                            pointStrokeColor = chartColorCZE,
                            pointHighlightFill = Colors.WHITE.color.toHTMLColor(),
                            pointHighlightStroke = chartColorCZE,
                            data = temperatureCZE),
                        LineChartSeries(
                            label = "Slovakia",
                            fillColor = chartColorSVK,
                            pointColor = chartColorSVK,
                            strokeColor = chartColorSVK,
                            pointStrokeColor = chartColorSVK,
                            pointHighlightFill = Colors.WHITE.color.toHTMLColor(),
                            pointHighlightStroke = chartColorSVK,
                            data = temperatureSVK)))

    var jsChart:dynamic

    val options = object {
        val datasetFill = false;
        val multiTooltipTemplate = "<%=datasetLabel%> : <%= value %>";
        val responsive = true
    }

    chart.drawLineChart(chartData, options, { jsChart = it })

    fun updateChart() {
        jsChart.addData(array(Math.random()*15, Math.random()*18), "Added");
    }

    return div {
            h4 { +"Line Chart" }
            +chart
            btsButton(look = ButtonLook.PRIMARY, size = ButtonSize.SMALL, label = { +"Add Data" }, onclick = ::updateChart)
        }

}