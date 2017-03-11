package com.foo.library.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.foo.library.model.EventType;
import com.foo.library.model.Subscriber;
import com.foo.library.model.Watcher;
import com.foo.library.model.Watcher.WatcherPK;
import com.foo.library.repository.SubscriberJpaRepository;
import com.foo.library.repository.WatcherJpaRepository;
import com.foo.library.service.SubscriptionService;

@Component
public class SubscriptionServiceImpl implements SubscriptionService {

	@Autowired
	private SubscriberJpaRepository subscriberJpaRepository;

	@Autowired
	private WatcherJpaRepository watcherJpaRepository;
	
	@Override
	public void subscribeForNewAdditions(String userId) {
		Subscriber subscriber = new Subscriber(userId,
				EventType.NEW_BOOK_CATALOG);
		subscriberJpaRepository.saveAndFlush(subscriber);
	}

	@Override
	public List<Subscriber> getSubscribersForNewAdditions() {
		return subscriberJpaRepository
				.findByEventType(EventType.NEW_BOOK_CATALOG);
	}

	@Override
	public void watchForBookCatalog(String userId, Long bookCatalogId) {
		WatcherPK watcherPK = new WatcherPK(userId, bookCatalogId);
		Watcher watcher = new Watcher();
		watcher.setId(watcherPK);
		watcherJpaRepository.saveAndFlush(watcher);
	}

	@Override
	public List<Watcher> getWatchers(Long bookCatalogId) {
		return watcherJpaRepository.findByBookCatalogId(bookCatalogId);
	}

}