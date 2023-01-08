function makeAjaxPostCall() {

  event.preventDefault();
  ajaxPost();

  };

  function ajaxPost() {

      var ipAddress = $("#ip").val();

      var formData = {
          ip : ipAddress
      }


  $.ajax({

    type: "POST",
    contentType : "application/json",
    url : "http://localhost:9090/checkIp",
    data : JSON.stringify(formData),
    dataType : 'json',
    success : function(result) {
        if (result == "Similar IP exists on the DB and can't be used") {
            var div = document.getElementById('ipResult');
            div.innerHTML = "Similar IP exists on the DB and can't be used";
        }

    }

    )};

  }

