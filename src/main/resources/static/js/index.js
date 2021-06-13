function toAzn() {
    let valute = document.getElementById("inputValute").value;
    let changeRate = document.getElementById("selectSecondValute").value;

    document.getElementById("inputAZN").value = valute*changeRate;
}

function toValute() {
    let valute = document.getElementById("inputAZN").value;
    let changeRate = document.getElementById("selectSecondValute").value;

    document.getElementById("inputValute").value = valute/changeRate;
}
