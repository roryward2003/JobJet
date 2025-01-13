# Step 1: Build the React app
FROM node:16 AS build

WORKDIR /app

COPY package.json ./
RUN npm install
COPY . ./
RUN npm run build

# Step 2: Serve the built app using Nginx
FROM nginx:alpine

# Copy the custom nginx configuration file
COPY nginx.conf /etc/nginx/nginx.conf

# Copy the build files to the nginx container
COPY --from=build /app/build /usr/share/nginx/html

# Expose port 3000 to access the app
EXPOSE 80

# Start nginx server
CMD ["nginx", "-g", "daemon off;"]