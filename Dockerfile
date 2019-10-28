FROM ubuntu

RUN apt-get update \
    && apt-get install -y wget curl make git openjdk-8-jre

RUN wget https://download.clojure.org/install/linux-install-1.10.1.469.sh \
    && chmod +x linux-install-1.10.1.469.sh \
    && ./linux-install-1.10.1.469.sh 

RUN wget -O pgfutter https://github.com/lukasmartinelli/pgfutter/releases/download/v1.2/pgfutter_linux_amd64 \
    && chmod +x pgfutter \
    && mv pgfutter /usr/local/bin

RUN wget http://ftp.gnu.org/gnu/parallel/parallel-latest.tar.bz2 \
    && tar -xvjf parallel-latest.tar.bz2 > extracted-files \
    && cd $(head -n 1 extracted-files) \ 
    && ./configure && make && make install \
    && cd .. \
    && rm -r extracted-files parallel-*

RUN git clone https://github.com/cjbarre/tsv2csv.git \
    && chmod +x ./tsv2csv/tsv2csv \
    && mv ./tsv2csv/tsv2csv /usr/local/bin \
    && rm -r tsv2csv

RUN apt-get install -y postgresql-client

RUN apt-get remove --purge -y curl make git && apt-get clean

RUN useradd -ms /bin/bash macroscope_admin

WORKDIR /home/macropscope_admin

COPY . macroscope

RUN chmod +x macroscope/bin/* && chown -R macroscope_admin macroscope

USER macroscope_admin

ENTRYPOINT cd macroscope && ./bin/init.sh


