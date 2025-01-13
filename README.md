
# THIS REPO IS A CLONE OF A GITLAB REPO

# csu33012-ay2425-project17

## Usage

### API Key Instructions

1. To obtain a Jooble API key, visit https://jooble.org/api/about and enter your details, adding scss.tcd.ie is sufficient
to get an API key. To add your API key as an environment variable follow the below instructions:

2. Create a file named .env in the root directory

3. Place your API KEY in the .env file in the following format:
    JOOBLE_API_KEY=your_api_key

### Run the program

1. Install docker desktop form the internet

2. Run the following command: (This may take a few minutes to build)
- docker-compose up --build

3. Go to http://localhost in your browser

4. For future executions you can leave out the --build flag in the docker-compose up command

## Alternative Usage

1. Set up API key environment variable as before

2. In your terminal execute the following commands:
- cd backend
- ./mvnw spring-boot:run

3. In a seperate termainal execute the following commands:
- cd frontend
- yarn
- yarn start

4. Go to http://localhost:3000 in your browser

## Description
JobJet is a job finding platform which emphasises simplicity. Jobs are searched through a few simple parameters, then
just swipe left or right!

## Support
Email us at jobj3t@gmail.com for support

## Team
- Palak Aggarwal
- Ollie Easterbrook
- Sarah Foley
- Jack Herssens
- Rory Ward