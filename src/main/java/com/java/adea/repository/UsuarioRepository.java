package com.java.adea.repository;

import com.java.adea.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, String> {

    //Aqui se valida que el usuario exista
    Optional<Usuario> findByLogin(String login);

    //Busquedas por estatus
    List<Usuario> findByStatus(char status);

    //Busqueda por nombre
    List<Usuario> findByNombre(String nombre);

    //Busqueda por fecha de alta inicial
    List<Usuario> findByFechaAlta(Date fechaAlta);

    //Busqueda por fecha de alta final
    List<Usuario> findByFechaBaja(Date fechaBaja);

}
