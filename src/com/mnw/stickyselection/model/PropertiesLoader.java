package com.mnw.stickyselection.model;

/**
 * TODO description of this class is missing
 */
public interface PropertiesLoader {
    ValuesRepositoryReader loadFromPermanentStorageOrDefault(DefaultValues defaultValues);
}
