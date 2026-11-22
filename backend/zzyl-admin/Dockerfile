# 基础镜像
FROM openjdk:11.0-jre-buster
# 设定时区
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
# 阿里云 OSS 密钥请通过 docker run -e 或 CI Secret 传入，不要写进镜像
ENV OSS_ACCESS_KEY_ID ""
ENV OSS_ACCESS_KEY_SECRET ""
# 拷贝jar包
COPY zzyl-admin.jar /app.jar
# 入口
ENTRYPOINT ["java", "-jar", "/app.jar"]