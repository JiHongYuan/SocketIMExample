$(document).ready(function () {
    var host = "http://192.168.3.4:8080";
    var userId;
    // 聊天的用户id
    var talkUserId;
    var index = {
        config: {},
        init: function () {
            // 模态框默认显示
            $('#modal').modal('show');
        },
        bind: function () {
            $("#btn-ok").on("click", function () {
                var id = $("#user-id").val();
                userId = id;

                $("#login-title").append(id);
                $("#modal").modal('toggle');

                // 启动一个socket client
                $.ajax({
                    url: host + "/socket-client/start",
                    type: "POST",
                    data: {
                        "userId": userId,
                    },
                    dataType: "json",
                    success: function (rs) {
                        console.log(rs);
                        index.initTimer.initUsers();
                    }
                })
            });

            $("#login-close").on("click", function () {
                $.ajax({
                    url: host + "/socket-client/close",
                    type: "POST",
                    data: {
                        "userId": userId
                    },
                    dataType: "json",
                    success: function (rs) {
                        console.log(rs);
                    }
                })
            });


            $(document).on("click", ".list-user", function () {
                $("#talk-title").text($(this).text());
                talkUserId = $(this).text();
                index.initTimer.initMessage();
            });

            $("#send").on("click", function () {

                $.ajax({
                    url: host + "/socket-client/send-message",
                    type: "POST",
                    data: JSON.stringify({
                        "userId": userId,
                        "sendUserId": talkUserId,
                        "message": $("#talk-info").val()
                    }),
                    contentType: "application/json",
                    dataType: "json",
                    success: function (rs) {
                        $("#talk-info").val("")
                    }
                })
            });
        },
        initTimer: {
            initUsers: function () {
                setInterval(this.timerUsers, 1000);
            },
            initMessage: function () {
                setInterval(this.timerMessage, 1000);
            },
            timerUsers: function () {
                // 定时获取存活用户列表
                $.ajax({
                    url: host + "/socket-client/get-users",
                    type: "GET",
                    data: {},
                    dataType: "json",
                    success: function (rs) {
                        console.log(rs);
                        var text = "";
                        if (rs.code === 20000) {
                            for (var i = 0; i < rs.data.length; i++) {
                                if (rs.data[i] === userId) {
                                    continue;
                                }
                                text += "<p class=\"list-group-item card-text list-user \" >" + rs.data[i] + "</p>";
                            }
                        }
                        $("#users").html(text);
                    }
                });
            },
            timerMessage: function () {
                $.ajax({
                    url: host + "/socket-client/get-message",
                    type: "GET",
                    data: {userId: userId, talkUserId: talkUserId},
                    dataType: "json",
                    success: function (rs) {
                        console.log(rs);
                        var html = "";
                        if (rs.code === 20000) {
                            for (var i = 0; i < rs.data.length; i++) {
                                var data = rs.data[i];
                                if (data.state === 1) {
                                    // 接收
                                    html += "<div class=\"alert alert-primary card-body\" role=\"alert\">"
                                    html += "<div>" + data.userId + "  " + data.date + "</div>";
                                } else if (data.state === 2) {
                                    // 发送
                                    html += "<div class=\"alert alert-success card-body\" role=\"alert\">";
                                    html += "<div>我 " + data.date + "</div>";

                                }

                                html += "<div>" + data.message + "</div>";
                                html += "</div>";
                            }
                        }
                        $("#talk-text").html(html);
                    }
                })
            }
        },

    };

    index.init();
    index.bind();
    window.onbeforeunload = function (e) {
        $("#login-close").on("click", function () {
            $.ajax({
                url: host + "/socket-client/close",
                type: "POST",
                data: {
                    "userId": userId
                },
                dataType: "json",
                success: function (rs) {
                    console.log(rs);
                }
            })
        });
    };
});