<html>
<head>
  <title> Laravel Coin Collection App </title>
  <link href="{{ mix('css/app.css') }}" rel="stylesheet" type="text/css">
</head>
<body>
  <div id="app">
    <nav class="nav navbar navbar-dark bg-dark">
      <div class="navbar-header">
        <a href="{{ route('home') }}" class="navbar-brand"> My Coin Collection </a>
      </div>
    </nav>
    @yield('content')
  </div>
  <script src="{{ mix('js/app.js') }}"></script>
</body>
</html>
