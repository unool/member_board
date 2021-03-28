//
// const baseSubAdd = '/base/chat';
//
// var email = [[${session['user'].email}]];
//
//
// $(function () {
//     var chatManager = (function () {
//         function ChatManager() {
//             chatManager.stompClient = null;
//             chatManager.roomAdd = '';
//             chatManager.myEmail = '';
//             chatManager.connected = false;
//
//
//         }
//     })
// })
//
// function connected(result){
//     chatManager.connected = result;
// }
//
// function subCallback(msg){
//     // showGreeting(JSON.parse(msg.body).content);
//     console.log("sub 콜백")
// }
//
//
//
// function connect() {
//     var socket = new SockJS('/ws_connect');
//     chatManager.stompClient = Stomp.over(socket);
//     chatManager.stompClient.connect({userEmail: chatManager.myEmail}, function (frame) {
//         connected(true);
//         console.log('결과 : ' + frame);
//         chatManager.stompClient.subscribe(baseSubAdd, function (msg) {
//             subCallback(msg);
//         });
//     });
// }
//
// function disconnect() {
//     if (stompClient !== null) {
//         stompClient.disconnect();
//     }
//     setConnected(false);
//     console.log("Disconnected");
// }
//
// function sendName() {
//     stompClient.send("/app/hello", {}, JSON.stringify({'name': $("#name").val()}));
// }
//
// function showGreeting(message) {
//     $("#greetings").append("<tr><td>" + message + "</td></tr>");
// }
//
// $(function () {
//     $("form").on('submit', function (e) {
//         e.preventDefault();
//     });
//     $( "#connect" ).click(function() { connect(); });
//     $( "#disconnect" ).click(function() { disconnect(); });
//     $( "#send" ).click(function() { sendName(); });
// });