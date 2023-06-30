<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>My Shop</title>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.2.0/jquery.min.js"></script>
</head>
<body>

<form method="post">
    <c:if test="${!connected}">
        <button type="submit" name="btnConnect" value= "true">ConnectProducts</button>
    </c:if>

    <c:if test="${connected}">
            <div id="catalog">
            ${catalog}
            </div>

            <br>
            <button type ="submit" name = "btnConnect" value = "false">Disconnect</button>
            <br>
            <input type ="button" id="btnRefresh" value= "Refresh"/>
    </c:if>
</form>

<script>
    $(document).ready(function(){
        $('#btnRefresh').click(function(){
                    $.ajax({'/products',
                        {type: 'PUT',
                        success:function(catalog) {
                        $('#catalog').html(catalog);
                        },
                        error: function(catalog){
                        $('#catalog').append('error'}
                });
        });
    });
</script>
</body>
</html>