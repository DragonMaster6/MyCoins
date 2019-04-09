<!DOCTYPE html>
<html lang="en" dir="ltr">
  <head>
    <meta charset="utf-8">
    <meta name="csrf-token" content="{{ csrf_token() }}">
    <title>My Coin Collection Manager App</title>

    <link rel="stylesheet" href="{{ mix('css/app.css') }}">
  </head>
  <body>
    <div id="app">
      <nav class="navbar navbar-dark bg-dark">
        <a class="navbar-brand" href="#">
          Brand Name
        </a>
        <ul class="navbar-nav">
          <li class="navbar-item"> Home </li>
        </ul>
      </nav>

      <!-- Display the rest of the website -->
      @yield('content')
    </div>

    <script src="{{ mix('js/app.js') }}"></script>
  </body>
</html>
