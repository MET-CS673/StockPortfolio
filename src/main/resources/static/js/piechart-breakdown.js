document.addEventListener("DOMContentLoaded", ready);

function ready() {

    console.log(JSON.stringify(mydata));
    console.log(keys);
    console.log(values);

    const data = {
        labels: keys,
        datasets: [{
            label: 'My First Dataset',
            data: values,
            backgroundColor: [
                "red",
                "green",
                "blue",
                "black",
                "yellow"
            ],
            hoverOffset: 4
        }]
    };

    const config = {
        type: 'pie',
        data: data,
    };

    var myChart = new Chart(
        document.getElementById('myChart'),
        config
    );

    var chart = am4core.create("amchart-container", am4charts.PieChart);

    // Add data
    chart.data = [{
        "country": "Lithuania",
        "litres": 501.9
    }, {
        "country": "Czech Republic",
        "litres": 301.9
    }, {
        "country": "Ireland",
        "litres": 201.1
    }, {
        "country": "Germany",
        "litres": 165.8
    }, {
        "country": "Australia",
        "litres": 139.9
    }, {
        "country": "Austria",
        "litres": 128.3
    }, {
        "country": "UK",
        "litres": 99
    }, {
        "country": "Belgium",
        "litres": 60
    }, {
        "country": "The Netherlands",
        "litres": 50
    }];

    // Add and configure Series
    var pieSeries = chart.series.push(new am4charts.PieSeries());
    pieSeries.dataFields.value = "litres";
    pieSeries.dataFields.category = "country";
    pieSeries.slices.template.stroke = am4core.color("#4a2abb");
    pieSeries.slices.template.strokeWidth = 2;
    pieSeries.slices.template.strokeOpacity = 1;
}
