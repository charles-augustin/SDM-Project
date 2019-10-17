package com.sdmproject.repository;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.text.WordUtils;
import org.springframework.stereotype.Repository;

import com.sdmproject.exceptions.DuplicateEntryException;
import com.sdmproject.model.Reservation;
import com.sdmproject.model.ReservationHistory;
import com.sdmproject.model.Vehicle;

@Repository
public class ReservationHistoryRepository {

	private List<ReservationHistory> records = new ArrayList<ReservationHistory>();
	
	public ReservationHistory save(ReservationHistory record) {
		records.add(record);
		return record;
	}
	
	public List<ReservationHistory> findAllWithSort(Optional<String> sortProperty, Optional<String> sortOrder) {
		List<ReservationHistory> result = records.stream().collect(Collectors.toList());
		if (sortProperty.isPresent()) {

			Collections.sort(result, new Comparator<Object>() {
				public int compare(Object o1, Object o2) {
					try {
						Method m = o1.getClass().getMethod("get" + WordUtils.capitalize(sortProperty.get()));
						if (sortProperty.get().contains("Date")) {
							Date s1 = (Date) m.invoke(o1);
							Date s2 = (Date) m.invoke(o2);
							return s1.compareTo(s2);
						} else if (sortProperty.get().contains("id") || sortProperty.get().contains("year")) {
							Integer s1 = (Integer) m.invoke(o1);
							Integer s2 = (Integer) m.invoke(o2);
							return s1.compareTo(s2);
						} else {
							String s1 = (String) m.invoke(o1);
							String s2 = (String) m.invoke(o2);
							return s1.compareTo(s2);
						}
					} catch (SecurityException e) {
						throw new RuntimeException(e);
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (NoSuchMethodException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return 0;
				}
			});

			if (sortOrder.get().equals("desc")) {
				Collections.reverse(result);
			}
		}

		return result;
	}

	private void sort(String sortProperty, List<ReservationHistory> result) {
		Collections.sort(result, new Comparator<Object>() {
			public int compare(Object o1, Object o2) {
				try {
					Method m = o1.getClass().getMethod("get" + WordUtils.capitalize(sortProperty));
					if (sortProperty.contains("year") || sortProperty.contains("id")) {
						Integer s1 = (Integer) m.invoke(o1);
						Integer s2 = (Integer) m.invoke(o2);
						return s1.compareTo(s2);
					} else {
						String s1 = (String) m.invoke(o1);
						String s2 = (String) m.invoke(o2);
						return s1.compareTo(s2);
					}
				} catch (SecurityException e) {
					throw new RuntimeException(e);
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return 0;
			}
		});
	}

	public List<ReservationHistory> filterMultipleAttribute(Map<String, String> map, Optional<String> sortProperty, Optional<String> order) {
		List<ReservationHistory> filteredResult = records.stream().collect(Collectors.toList());
		if (map.isEmpty()) {
			return filteredResult;
		}
		for (Map.Entry<String, String> entry : map.entrySet()) {
			filteredResult = filteredResult.stream().filter(item -> {
				try {
					return (predicateEvaluation(item, entry.getKey(), entry.getValue()));
				} catch (IllegalAccessException | SecurityException | NoSuchMethodException | IllegalArgumentException
						| InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return false;
			}).collect(Collectors.toList());
		}
		
		if(sortProperty.isPresent())
			sort(sortProperty.get(), filteredResult);
		
		return filteredResult;
	}

	public boolean predicateEvaluation(ReservationHistory item, String key, String val) throws NoSuchMethodException,
			SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method m = item.getClass().getMethod("get" + WordUtils.capitalize(key));
		
			return ((String) m.invoke(item)).equalsIgnoreCase(val);
		
	}

	
	public List<ReservationHistory> findAll() {
		return records;
	}

}
