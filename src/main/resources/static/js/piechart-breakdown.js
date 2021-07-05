document.addEventListener("DOMContentLoaded", ready);

function ready() {

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
}
