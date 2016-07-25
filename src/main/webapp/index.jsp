<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>上传文件</title>
    <script type="text/javascript">

    </script>
</head>
<body>
<h4>上传文件1</h4>
<form name="userForm" action="../file/upload2" method="post" enctype="multipart/form-data" >
    选择文件：<input type="file" name="file">
    <input type="submit" value="上传" >
</form>
</body>
</html>