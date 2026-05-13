package com.example.VideoRent.service;

import com.example.VideoRent.dao.CopyDao;
import com.example.VideoRent.dao.RentalDao;
import com.example.VideoRent.dao.UserDao;
import com.example.VideoRent.entity.Copy;
import com.example.VideoRent.entity.Rental;
import com.example.VideoRent.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
public class RentalService {

    private final RentalDao rentalDao;
    private final UserDao userDao;
    private final CopyDao copyDao;

    public RentalService(RentalDao rentalDao, UserDao userDao, CopyDao copyDao) {
        this.rentalDao = rentalDao;
        this.userDao = userDao;
        this.copyDao = copyDao;
    }

    public Rental getRental(Long id) {
        return rentalDao.getById(id);
    }

    public List<Rental> getRentalsByUser(Long userId) {
        return rentalDao.findByUserId(userId);
    }

    @Transactional
    public void requestExtension(Long rentalId, LocalDate requestedDate) {
        Rental rental = rentalDao.getById(rentalId);
        if (rental == null || rental.getReturnDate() != null) return;
        rental.setExtensionRequested(true);
        rental.setRequestedDate(requestedDate);
    }

    public long countActiveRentals() {
        return rentalDao.countActive();
    }

    @Transactional
    public void returnRental(Long rentalId) {
        Rental rental = rentalDao.getById(rentalId);
        if (rental == null || rental.getReturnDate() != null) return;
        rental.setReturnDate(LocalDate.now());
        rental.getCopy().setCount(rental.getCopy().getCount() + 1);
    }

    @Transactional
    public boolean returnByMovieAndPhone(Long movieId, String phone) {
        Rental rental = rentalDao.findActiveByMovieAndPhone(movieId, phone);
        if (rental == null) return false;
        rental.setReturnDate(LocalDate.now());
        rental.getCopy().setCount(rental.getCopy().getCount() + 1);
        return true;
    }

    @Transactional
    public void approveExtension(Long rentalId) {
        Rental rental = rentalDao.getById(rentalId);
        if (rental == null || rental.getRequestedDate() == null) return;
        rental.setDueDate(rental.getRequestedDate());
        rental.setExtensionRequested(false);
        rental.setRequestedDate(null);
    }

    @Transactional
    public void rejectExtension(Long rentalId) {
        Rental rental = rentalDao.getById(rentalId);
        if (rental == null) return;
        rental.setExtensionRequested(false);
        rental.setRequestedDate(null);
    }

    @Transactional
    public IssueResult issue(Long copyId, String phone, LocalDate dueDate) {
        if (dueDate == null || !dueDate.isAfter(LocalDate.now())) return IssueResult.INVALID_DATE;
        User user = userDao.findByPhone(phone);
        if (user == null) return IssueResult.USER_NOT_FOUND;
        Copy copy = copyDao.getById(copyId);
        if (copy == null || copy.getCount() <= 0) return IssueResult.NO_COPIES;
        Rental rental = new Rental();
        rental.setUser(user);
        rental.setCopy(copy);
        rental.setIssueDate(LocalDate.now());
        rental.setDueDate(dueDate);
        rental.setExtensionRequested(false);
        rentalDao.save(rental);
        copy.setCount(copy.getCount() - 1);
        return IssueResult.SUCCESS;
    }
}
