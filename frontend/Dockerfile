# Stage 1: Build and test Angular application
FROM node:20-alpine AS builder
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
RUN npm run test  # Runs Jest tests
RUN npm run build -- --configuration production

# Stage 2: Production server
FROM nginx:alpine
RUN rm -rf /etc/nginx/conf.d/*
COPY --from=builder /app/dist/frontend/browser /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
