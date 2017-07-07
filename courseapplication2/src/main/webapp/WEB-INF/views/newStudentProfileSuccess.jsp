<%@ include file="./include.jsp"%>
<html>
<head>
<title>New Student Profile Created</title>
</head>
<body>

<h2>A new profile has been successfully created for:
      <font color="blue"/>${student.firstName} ${student.lastName} </font></h2>
<br>
<h2>
<a href="${context}">Home</a>
</h2>
      
</body>
</html>
