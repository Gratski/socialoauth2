# Social OAuth2 Client
### Updated to be fully working for Web and Mobile clients using **Java 17** and SpringBoot 3.2.3.

This project uses a PostgreSQL Database to store the new users and update the information of the already existing ones.<br/>
**RECOMMENDATION: Start by configuring the Github OAuth2 Client. It's the most straight forward one.**<br/>
**SUGGESTION: If you need a public and free https connection for testing purposes you can use: https://ngrok.com/ (mandatory for Facebook)**<br/>

### Assumptions
- Java 17 SDK is installed (or set to your IDE).
- Maven is correctly installed.
- Docker is correctly installed.

#### Step 1::Run the database
From your terminal or prompt run: 
- ```docker-compose -f images/postgres_compose.yml up```

Open your preferred database client using the connection details present in the images/docker_compose.yml file and run the following script:
- src/main/resources/db/migrations/V1__setup.sql

#### Step 2::Change configuration files
In the src/main/resources/application.yml you will find SpringBoot configuration as well as project's specific configurations.
These being:
```
security:
    oauth2:
      client:
        registration:
          google:
            clientId: <GOOGLE_KEY>
            clientSecret: <GOOGLE_SECRET>
            redirectUri: "<BASE_URL>/oauth2/callback/google"
            scope:
              - email
              - profile
          facebook:
            clientId: <FACEBOOK_KEY>
            clientSecret: <FACEBOOK_SECRET>
            redirectUri: "<BASE_URL>/oauth2/callback/facebook"
            scope:
              - email
              - public_profile
          github:
            clientId: <GITHUB_KEY>
            clientSecret: <GITHUB_SECRET>
            redirectUri: "<BASE_URL>/oauth2/callback/github"
            scope:
              - user:email
              - read:user
```
**NOTE: None of the providers is mandatory. Configure only the ones you need/want to.**<br/>
Where you should replace **clientId**, **clientSecret** and **redirectUri** for each of the providers you want.

Here are the links where you should configure OAuth2 Clients:
- Github: https://github.com/settings/apps
- Google: https://console.developers.google.com
- Facebook: https://developers.facebook.com/apps

#### Step 3::Test it
Use any server of you preference, but I recommend you use NodeJS with Express as it is very easy an simple to put a PoC in place.<br>
And serve 2 pages, one for the login/signup buttons and another one for the login/signup confirmation.<br/>

##### Useful code snippets for this step
Server
```
const express = require('express');
const path = require('path');

const app = express();
const port = process.env.PORT || 3000;

app.get('/', function(req, res) {
  res.sendFile(path.join(__dirname, '/src/pages/index.html'));
});

app.get('/oauth2/redirect', function(req, res) {
    res.sendFile(path.join(__dirname, '/src/pages/login_callback.html'));
  });

app.listen(port);
console.log('Server started at http://localhost:' + port);
```

Html Page with the Forms index.html
```
<html>
    <body>
        <form method="post" action="http://localhost:8080/oauth2/authorize/github?redirect_uri=http://localhost:3000/oauth2/redirect">
            <button type="submit">Github</button>
        </form>
        <br/>
        <form method="post" action="https://ee6e-89-154-156-34.ngrok-free.app/oauth2/authorize/google?redirect_uri=https://8807-89-154-156-34.ngrok-free.app/oauth2/redirect">
            <button type="submit">Google</button>
        </form>
        <br/>
        <form method="post" action="https://ee6e-89-154-156-34.ngrok-free.app/oauth2/authorize/facebook?redirect_uri=https://8807-89-154-156-34.ngrok-free.app/oauth2/redirect">
            <button type="submit">Facebook</button>
        </form>
    </body>
</html>
```

Html Page with the login/signup confirmation
```
<html>
    <body>
        Login Successfull!
    </body>
</html>
```

