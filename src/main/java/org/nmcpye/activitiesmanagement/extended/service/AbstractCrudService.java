package org.nmcpye.activitiesmanagement.extended.service;

import org.nmcpye.activitiesmanagement.extended.common.BaseIdentifiableObject;
import org.nmcpye.activitiesmanagement.extended.common.IdentifiableObjectStore;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * Created by Hamza on 15/11/2021.
 */
public abstract class AbstractCrudService<T extends BaseIdentifiableObject, ID extends Serializable>
    implements CrudService<T, ID> {

    protected JpaRepository<T, ID> repository;

    public AbstractCrudService(JpaRepository<T, ID> repository) {
        this.repository = repository;
    }

    @Override
    public Long add(T entity) {
        return repository.save(entity).getId();
    }

    @Override
    public void update(T entity) {
        ((IdentifiableObjectStore<T>)repository).update(entity);
    }

    @Override
    public void delete(T entity) {
        repository.delete(entity);
    }

    @Override
    public T get(ID id) {
        return repository.getOne(id);
    }

    @Override
    public T getByUid(String uid) {
        return ((IdentifiableObjectStore<T>)repository).getByUid(uid);
    }

    @Override
    public T getByCode(String code) {
        return ((IdentifiableObjectStore<T>)repository).getByCode(code);
    }

    @Override
    public List<T> getAll() {
        return null;
    }

    @Override
    public List<T> getById(Collection<Long> identifiers) {
        return ((IdentifiableObjectStore<T>)repository).getById(identifiers);
    }

    @Override
    public List<T> getByUid(Collection<String> uids) {
        return ((IdentifiableObjectStore<T>)repository).getByUid(uids);
    }

    @Override
    public List<T> getByName(Collection<String> names) {
        return ((IdentifiableObjectStore<T>)repository).getByName(names);
    }

    @Override
    public T getByName(String name) {
        return ((IdentifiableObjectStore<T>)repository).getByName(name);
    }

    @Override
    public List<T> getAllLikeName(String name) {
        return ((IdentifiableObjectStore<T>)repository).getAllLikeName(name);
    }

    @Override
    public Iterable<T> findAll(Sort sort) {
        return repository.findAll(sort);
    }

    @Override
    public Page<T> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }
}
