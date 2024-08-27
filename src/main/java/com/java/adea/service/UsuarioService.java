package com.java.adea.service;

import com.java.adea.Exception.DataBaseException;
import com.java.adea.entity.Usuario;
import com.java.adea.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.GrantedAuthority;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario findByLogin(String login) {
        try {
            return usuarioRepository.findByLogin(login)
                    .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con login: " + login));
        } catch (DataAccessException e) {
            throw new DataBaseException("Error al acceder a los datos del usuario", e);
        }
    }

    public String encriptarConSHA256(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encodedhash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al encriptar la contraseña", e);
        }
    }

    public List<Usuario> findStatus(Character status) {
        return usuarioRepository.findByStatus(status);
    }

    public List<Usuario> findNombre(String numbre) {
        return usuarioRepository.findByNombre(numbre);
    }

    public List<Usuario> findFechaAlta(Date fecha) {
        return usuarioRepository.findByFechaAlta(fecha);
    }

    public List<Usuario> findFechaBaja(Date fecha) {
        return usuarioRepository.findByFechaBaja(fecha);
    }

    public List<Usuario> findAllUser() {
        return usuarioRepository.findAll();
    }

    @Transactional
    public Usuario create(Usuario usuario) {
        usuario.setPassword(encriptarConSHA256(usuario.getPassword()));
        LocalDate currentDate = LocalDate.now();
        usuario.setFechaAlta(currentDate);
        usuario.setFechaModificacion(currentDate);
        if (usuario.getFechaVigencia().isBefore(currentDate)) {
            usuario.setStatus('A');
        } else {
            usuario.setStatus('B');
        }
        usuario.setStatus('A');
        usuario.setIntentos(0.0f);
        return usuarioRepository.save(usuario);
    }

    public Usuario update(Usuario usuario) {
        usuario.setPassword(encriptarConSHA256(usuario.getPassword()));
        LocalDate currentDate = LocalDate.now();
        usuario.setFechaModificacion(currentDate);
        if (usuario.getStatus() != 'R' && usuario.getFechaVigencia().isAfter(currentDate)) {
            usuario.setStatus('A');
        } else {
            usuario.setStatus('B');
        }
        return usuarioRepository.save(usuario);
    }

    public boolean delete(Usuario usuario) {
        try {
            usuarioRepository.delete(usuario);
            return true;
        } catch (DataAccessException e) {
            System.err.println("Error al eliminar el usuario: " + e.getMessage());
            return false;
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = findByLogin(username);
        if(usuario == null) {
            throw new UsernameNotFoundException("Usuario inválido");
        }
        // Verificar la fecha de vigencia
        LocalDate currentDate = LocalDate.now();
        if (usuario.getFechaVigencia() != null && usuario.getFechaVigencia().isBefore(currentDate)) {
            throw new UsernameNotFoundException("La fecha de vigencia ha expirado");
        }
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");
        return new User(usuario.getLogin(),usuario.getPassword(), Collections.singletonList(authority));
    }
}
