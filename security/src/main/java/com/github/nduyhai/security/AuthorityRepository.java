package com.github.nduyhai.security;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "authority")
public interface AuthorityRepository extends JpaRepository<AuthorityEntity, Integer> {
}
