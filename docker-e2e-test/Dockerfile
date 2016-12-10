FROM esycat/java

MAINTAINER "Oli Zimpasser" <oglimmer@gmail.com>

# BUILD TOOLS

RUN apt-get update && \
    apt-get -qy install maven git npm phantomjs && \
    apt-get clean && \
    rm -rf /tmp/* /var/tmp/* /var/lib/apt/archive/* /var/lib/apt/lists/*

RUN ln -s /usr/bin/nodejs /usr/bin/node

RUN npm install -g jasmine phantomjs-prebuilt

RUN mkdir -p /home/build

COPY entrypoint.sh /home/build
COPY base_data.xml /home/build

WORKDIR /home/build/

CMD ./entrypoint.sh
