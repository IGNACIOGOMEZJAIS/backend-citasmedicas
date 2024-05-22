package com.citasmedica.backend.backendcitasmedicas.services;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.citasmedica.backend.backendcitasmedicas.models.entities.Pacient;
import com.citasmedica.backend.backendcitasmedicas.repositories.PacientRepository;


@Service
public class JpaPacientDetailsService implements UserDetailsService {

    @Autowired
    private PacientRepository repository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Pacient> o = repository.findByDni(Long.parseLong(username));
        if (!o.isPresent()) {
            throw new UsernameNotFoundException(String.format("Usuario %s no encontrado", username));

        }
        Pacient pacient = o.orElseThrow();
        List<GrantedAuthority> authorities = pacient.getRoles()
                .stream()
                .map(r -> new SimpleGrantedAuthority(r.getName())).collect(Collectors.toList());
        return new User(pacient.getDni().toString(), pacient.getPassword()

                , true, true, true, true, authorities);

    }

}
