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
          Coin Collection
        </a>
        <ul class="navbar-nav mr-auto">
          <li class="navbar-item"> Home </li>
        </ul>
      </nav>

      <!-- Create a Master-View Layout -->
      <div class="container-full">
        <div class="row">
          <div class="master-container col-md-3">
            Quick Searching bar
          </div>
          <div class="detail-container col-md-9">
            @yield('content')
          </div>
        </div>
      </div>
    </div>

    <script src="{{ mix('js/app.js') }}"></script>
  </body>
</html>
