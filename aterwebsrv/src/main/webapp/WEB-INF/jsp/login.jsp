<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Login</title>
</head>
<body>
	<form action="<c:url value="/login"/>" method="post" role="form">
		<fieldset>
			<legend>
				<h2>Login</h2>
			</legend>
			<div class="form-group">
				<label for="username">Username:</label> <input id="username"
					class="form-control" type='text' name='username' value="marissa" />
			</div>
			<div class="form-group">
				<label for="password">Password:</label> <input id="password"
					class="form-control" type='text' name='password' value="koala" />
			</div>
			<button class="btn btn-primary" type="submit">Login</button>
			<input type="hidden" name="${_csrf.parameterName}"
				value="${_csrf.token}" />
		</fieldset>
	</form>
</body>
</html>