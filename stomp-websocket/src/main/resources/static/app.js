var clientWebSocket = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    disconnect();

    clientWebSocket = new WebSocket("ws://localhost:8080/event-emitter");

    clientWebSocket.onopen = function() {
        console.log("clientWebSocket.readyState");
        clientWebSocket.send("event-me-from-browser");
    }
    clientWebSocket.onclose = function() {
        console.log("Closing connection");
    }
    clientWebSocket.onerror = function(error) {
        console.log("An error occured", error);
    }
    clientWebSocket.onmessage = function(msg) {
        showGreeting(msg.data);
    }

    setConnected(true);
}

function disconnect() {
    if (clientWebSocket !== null) {
        clientWebSocket.close();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
    clientWebSocket.send(JSON.stringify({'name': $("#name").val()}));
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendName(); });
});