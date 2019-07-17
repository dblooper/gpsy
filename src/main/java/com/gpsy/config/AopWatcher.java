package com.gpsy.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Aspect
@Component
public class AopWatcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(AopWatcher.class);

    @Around("execution(public * com.gpsy.service.dbApiServices..*(..))")
    public Object logMeasureRuntime(final ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object result;
        try {
            double begin = System.currentTimeMillis();
            result = proceedingJoinPoint.proceed();
            double end = System.currentTimeMillis();
            LOGGER.info("Time consumed: " + ((end - begin)/1000) + "s by service [" +proceedingJoinPoint.getSignature().toShortString() + "]");
        }catch (Throwable throwable) {
            LOGGER.error(throwable.getMessage());
            throw throwable;
        }
        return result;
    }

    @AfterReturning("execution(public * com.gpsy.externalApis.spotify.client.SpotifyAuthorizator.authorize())")
    public void announceAuthorization() {
        LOGGER.info("A new authorization token has been successfully set!");
    }

    @AfterReturning("execution(public * com.gpsy.externalApis.spotify.client.SpotifyClient.updatePlaylistTracks(..))")
    public void announceSettingNewPlaylist() {
        LocalDateTime dateTime = LocalDateTime.now();
        String dateTimeFotmatted = dateTime.format(DateTimeFormatter.ofPattern("yy-MM-dd HH:mm:ss"));
        LOGGER.info("New playlist generated on Spotify successfully at: [" + dateTimeFotmatted + "]");
    }

    @AfterReturning("execution(public * com.gpsy.service.dbApiServices.spotify.SaveSpotifyDataToDbService.saveRecentPlayedTracks())")
    public void announceFetchingTracksFromSpotify() {
        LocalDateTime dateTime = LocalDateTime.now();
        String dateTimeFotmatted = dateTime.format(DateTimeFormatter.ofPattern("yy-MM-dd HH:mm:ss"));
        LOGGER.info("New set of recent tracks fetched to db successfully at: [" + dateTimeFotmatted + "]");

    }}
