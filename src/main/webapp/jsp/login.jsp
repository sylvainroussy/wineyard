<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Please sign in</title>
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-/Y6pD6FV/Vv2HJnA6t+vslU6fwYXjCFtcEpHbNJ0lyAFsXTsjBbfaDjzALeQsN6M" crossorigin="anonymous">
    <link href="https://getbootstrap.com/docs/4.0/examples/signin/signin.css" rel="stylesheet" crossorigin="anonymous"/>
    <style type="text/css">
    body {
			margin: 10px;
			padding: 10px;
			background: #78081e;
			color: white;
		}
	</style>
  </head>
  <body>
     <div class="container">
      <form class="form-signin" method="post" action="/login">
        <h2 class="form-signin-heading">Wineyard</h2>
<div class="alert alert-success" role="alert">Vous avez &eacute;t&eacute; d&eacute;connect&eacute;</div>        <p>
          <label for="username" class="sr-only">Email</label>
          <input type="text" id="username" name="username" class="form-control" placeholder="email" required autofocus>
        </p>
        <p>
          <label for="password" class="sr-only">Mot de passe</label>
          <input type="password" id="password" name="password" class="form-control" placeholder="mot de passe" required>
        </p>
        <button class="btn btn-lg btn-primary btn-block" type="submit">Entrer</button>
      </form>
</div>
</body></html>