FROM node:18-alpine
WORKDIR /
COPY . .
RUN yarn install
CMD ["node", "index.js"]
EXPOSE 3000
