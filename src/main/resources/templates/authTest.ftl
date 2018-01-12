<html>
<body>
 <form action="/authTestProc" method="post">
  
 <select name="ROLE_NAME">
  <option value ="admin">Administrator</option>
  <option value ="manager">Manager</option>
  <option value="operator">Operator</option>
  <option value="director">Director</option>
</select>

  <input type="submit" value="Auth Test" />
  
  <a href="/logout">Logout</a>  

  
</form>
 
</body>
</html>