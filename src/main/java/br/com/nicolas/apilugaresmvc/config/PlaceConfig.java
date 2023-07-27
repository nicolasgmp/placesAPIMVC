package br.com.nicolas.apilugaresmvc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import br.com.nicolas.apilugaresmvc.domain.repositories.PlaceRepository;
import br.com.nicolas.apilugaresmvc.domain.services.PlaceService;

@Configuration
@EnableJpaAuditing
public class PlaceConfig {

  @Bean
  PlaceService placeService(PlaceRepository placeRepository) {
    return new PlaceService(placeRepository);
  }
}
