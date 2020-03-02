<!DOCTYPE html>

<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
  <meta http-equiv="refresh" content="10">
  <title>Web Checkers | ${title}</title>
  <link rel="stylesheet" type="text/css" href="/css/style.css">
</head>

<body>
<div class="page">

  <h1>Web Checkers | ${title}</h1>

  <!-- Provide a navigation bar -->
  <#include "nav-bar.ftl" />

  <div class="body">

    <!-- Provide a message to the user, if supplied. -->
    <#include "message.ftl" />

    <h2>Players Online</h2>
    <p>${otherUsers}</p>
    <div class="playerList">
      <#if currentUser??>
        <form action="/" method="POST">
          <table border=1>
            <th>Current Players</th>
            <#list players as player>
              <tr>
                <td>
                  <input type="radio" id=${player} name="otherPlayer" value=${player}>
                  <label for=${player}>${player}</label><br>
                </td>
              </tr>
            </#list>
          </table>
          <input type="submit" value="Challenge">
        </form>
      </#if>
    </div>

    <!-- TODO: future content on the Home:
            to start games,
            spectating active games,
            or replay archived games
    -->

  </div>

</div>
</body>

</html>
