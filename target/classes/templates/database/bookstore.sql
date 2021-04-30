-- phpMyAdmin SQL Dump
-- version 5.0.3
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Mar 14, 2021 at 02:44 AM
-- Server version: 10.4.14-MariaDB
-- PHP Version: 7.3.23

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `bookstore`
--

-- --------------------------------------------------------

--
-- Table structure for table `address`
--

CREATE TABLE `address` (
  `ADDRESS_ID` int(11) NOT NULL,
  `FIRST_NAME` varchar(255) NOT NULL,
  `LAST_NAME` varchar(255) NOT NULL,
  `STREET` varchar(255) NOT NULL,
  `CITY` varchar(255) NOT NULL,
  `STATE` varchar(255) NOT NULL,
  `ZIP` int(5) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `book`
--

CREATE TABLE `book` (
  `BOOK_ID` int(11) NOT NULL,
  `TITLE` varchar(255) NOT NULL,
  `ISBN` varchar(13) NOT NULL,
  `AUTHOR_NAME` varchar(255) NOT NULL,
  `CATEGORIES` varchar(255) NOT NULL,
  `DESCRIPTION` varchar(255) NOT NULL,
  `COVER_PICTURE` varchar(1000) NOT NULL,
  `EDITION` int(11) NOT NULL,
  `PUBLISHER_NAME` varchar(50) NOT NULL,
  `PUBLISHER_YEAR` year(4) NOT NULL,
  `PRICE` double(10,2) NOT NULL,
  `QUANTITY` int(11) NOT NULL,
  `KEYWORDS` varchar(255) DEFAULT NULL,
  `STATUS` int(11) DEFAULT NULL,
  `QUANTITY_ON_HAND` int(11) DEFAULT NULL,
  `MINIMUM_THRESHOLD` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `cart`
--

CREATE TABLE `cart` (
  `CART_ID` int(11) NOT NULL,
  `USER_ID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `cart_item`
--

CREATE TABLE `cart_item` (
  `CART_ID` int(11) NOT NULL,
  `BOOK_ID` int(11) NOT NULL,
  `QUANTITY` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `order`
--

CREATE TABLE `order` (
  `ORDER_ID` int(11) NOT NULL,
  `USER_ID` int(11) NOT NULL,
  `PAYMENT_ID` int(11) NOT NULL,
  `SUBTOTAL` double(10,2) NOT NULL,
  `TOTAL_TAX` double(10,2) NOT NULL,
  `GRAND_TOTAL` double(10,2) NOT NULL,
  `PROMO_ID` int(11) NOT NULL,
  `ORDER_DATE_TIME` datetime NOT NULL,
  `SHIPPING_TYPE` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `order_item`
--

CREATE TABLE `order_item` (
  `ORDER_ID` int(11) NOT NULL,
  `BOOK_ID` int(11) NOT NULL,
  `QUANTITY` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `payment_card`
--

CREATE TABLE `payment_card` (
  `PAYMENT_ID` int(11) NOT NULL,
  `CARD_NUM` bigint(16) NOT NULL,
  `USER_ID` int(11) NOT NULL,
  `BILLING_ADDRESS` int(11) NOT NULL,
  `TYPE` varchar(255) NOT NULL,
  `EXP_DATE` date NOT NULL,
  `CCV` int(3) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `promotion`
--

CREATE TABLE `promotion` (
  `PROMO_ID` int(11) NOT NULL,
  `START_DATE` date NOT NULL,
  `END_DATE` date NOT NULL,
  `DISCOUNT` double(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `shipping_type`
--

CREATE TABLE `shipping_type` (
  `SHIPPING_ID` int(11) NOT NULL,
  `SHIPPING_NAME` varchar(255) NOT NULL,
  `SHIPPING_COST` double(10,2) NOT NULL,
  `SHIPPING_SPEED` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `shipping_type`
--

INSERT INTO `shipping_type` (`SHIPPING_ID`, `SHIPPING_NAME`, `SHIPPING_COST`, `SHIPPING_SPEED`) VALUES
(1, 'Standard Shipping', 4.99, '3-5 Business Days'),
(2, 'Express Shipping', 7.99, '2-3 Business Days'),
(3, 'Expedited Shipping', 15.99, '1-2 Business Days');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `USER_ID` int(11) NOT NULL,
  `USER_NAME` varchar(255) NOT NULL,
  `EMAIL_ADDRESS` varchar(255) NOT NULL,
  `PASSWORD` varchar(255) NOT NULL,
  `FIRST_NAME` varchar(255) NOT NULL,
  `LAST_NAME` varchar(255) NOT NULL,
  `PHONE_NUMBER` int(10) DEFAULT NULL,
  `BIRTHDAY` date DEFAULT NULL,
  `USER_STATUS` int(2) NOT NULL DEFAULT 2,
  `USER_TYPE` int(2) NOT NULL DEFAULT 2,
  `SHIPPING_ADDRESS` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `user_status`
--

CREATE TABLE `user_status` (
  `STATUS_ID` int(2) NOT NULL,
  `STATUS_NAME` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `user_status`
--

INSERT INTO `user_status` (`STATUS_ID`, `STATUS_NAME`) VALUES
(1, 'Inactive/Unregistered'),
(2, 'Active/Registered'),
(3, 'Suspended');

-- --------------------------------------------------------

--
-- Table structure for table `user_type`
--

CREATE TABLE `user_type` (
  `TYPE_ID` int(2) NOT NULL,
  `TYPE_NAME` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `user_type`
--

INSERT INTO `user_type` (`TYPE_ID`, `TYPE_NAME`) VALUES
(1, 'Admin'),
(2, 'Customer');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `address`
--
ALTER TABLE `address`
  ADD PRIMARY KEY (`ADDRESS_ID`);

--
-- Indexes for table `book`
--
ALTER TABLE `book`
  ADD PRIMARY KEY (`BOOK_ID`);

--
-- Indexes for table `cart`
--
ALTER TABLE `cart`
  ADD PRIMARY KEY (`CART_ID`),
  ADD KEY `USER_ID` (`USER_ID`);

--
-- Indexes for table `cart_item`
--
ALTER TABLE `cart_item`
  ADD KEY `CART_ID` (`CART_ID`),
  ADD KEY `BOOK_ID` (`BOOK_ID`);

--
-- Indexes for table `order`
--
ALTER TABLE `order`
  ADD PRIMARY KEY (`ORDER_ID`),
  ADD KEY `USER_ID` (`USER_ID`),
  ADD KEY `PAYMENT_ID` (`PAYMENT_ID`),
  ADD KEY `PROMO_ID` (`PROMO_ID`),
  ADD KEY `SHIPPING_TYPE` (`SHIPPING_TYPE`);

--
-- Indexes for table `order_item`
--
ALTER TABLE `order_item`
  ADD KEY `ORDER_ID` (`ORDER_ID`),
  ADD KEY `BOOK_ID` (`BOOK_ID`);

--
-- Indexes for table `payment_card`
--
ALTER TABLE `payment_card`
  ADD PRIMARY KEY (`PAYMENT_ID`),
  ADD KEY `USER_ID` (`USER_ID`),
  ADD KEY `BILLING_ADDRESS` (`BILLING_ADDRESS`);

--
-- Indexes for table `promotion`
--
ALTER TABLE `promotion`
  ADD PRIMARY KEY (`PROMO_ID`);

--
-- Indexes for table `shipping_type`
--
ALTER TABLE `shipping_type`
  ADD PRIMARY KEY (`SHIPPING_ID`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`USER_ID`),
  ADD UNIQUE KEY `USER_NAME` (`USER_NAME`),
  ADD UNIQUE KEY `EMAIL_ADDRESS` (`EMAIL_ADDRESS`),
  ADD KEY `USER_STATUS` (`USER_STATUS`),
  ADD KEY `USER_TYPE` (`USER_TYPE`),
  ADD KEY `SHIPPING_ADDRESS` (`SHIPPING_ADDRESS`);

--
-- Indexes for table `user_status`
--
ALTER TABLE `user_status`
  ADD PRIMARY KEY (`STATUS_ID`);

--
-- Indexes for table `user_type`
--
ALTER TABLE `user_type`
  ADD PRIMARY KEY (`TYPE_ID`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `address`
--
ALTER TABLE `address`
  MODIFY `ADDRESS_ID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `book`
--
ALTER TABLE `book`
  MODIFY `BOOK_ID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `cart`
--
ALTER TABLE `cart`
  MODIFY `CART_ID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `order`
--
ALTER TABLE `order`
  MODIFY `ORDER_ID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `payment_card`
--
ALTER TABLE `payment_card`
  MODIFY `PAYMENT_ID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `promotion`
--
ALTER TABLE `promotion`
  MODIFY `PROMO_ID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `USER_ID` int(11) NOT NULL AUTO_INCREMENT;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `cart`
--
ALTER TABLE `cart`
  ADD CONSTRAINT `cart_ibfk_1` FOREIGN KEY (`USER_ID`) REFERENCES `user` (`USER_ID`);

--
-- Constraints for table `cart_item`
--
ALTER TABLE `cart_item`
  ADD CONSTRAINT `cart_item_ibfk_1` FOREIGN KEY (`CART_ID`) REFERENCES `cart` (`CART_ID`),
  ADD CONSTRAINT `cart_item_ibfk_2` FOREIGN KEY (`BOOK_ID`) REFERENCES `book` (`BOOK_ID`);

--
-- Constraints for table `order`
--
ALTER TABLE `order`
  ADD CONSTRAINT `order_ibfk_1` FOREIGN KEY (`USER_ID`) REFERENCES `user` (`USER_ID`),
  ADD CONSTRAINT `order_ibfk_2` FOREIGN KEY (`PAYMENT_ID`) REFERENCES `payment_card` (`PAYMENT_ID`),
  ADD CONSTRAINT `order_ibfk_3` FOREIGN KEY (`PROMO_ID`) REFERENCES `promotion` (`PROMO_ID`),
  ADD CONSTRAINT `order_ibfk_4` FOREIGN KEY (`SHIPPING_TYPE`) REFERENCES `shipping_type` (`SHIPPING_ID`);

--
-- Constraints for table `order_item`
--
ALTER TABLE `order_item`
  ADD CONSTRAINT `order_item_ibfk_1` FOREIGN KEY (`ORDER_ID`) REFERENCES `order` (`ORDER_ID`),
  ADD CONSTRAINT `order_item_ibfk_2` FOREIGN KEY (`BOOK_ID`) REFERENCES `book` (`BOOK_ID`);

--
-- Constraints for table `payment_card`
--
ALTER TABLE `payment_card`
  ADD CONSTRAINT `payment_card_ibfk_1` FOREIGN KEY (`USER_ID`) REFERENCES `user` (`USER_ID`),
  ADD CONSTRAINT `payment_card_ibfk_2` FOREIGN KEY (`BILLING_ADDRESS`) REFERENCES `address` (`ADDRESS_ID`);

--
-- Constraints for table `user`
--
ALTER TABLE `user`
  ADD CONSTRAINT `user_ibfk_1` FOREIGN KEY (`USER_STATUS`) REFERENCES `user_status` (`STATUS_ID`),
  ADD CONSTRAINT `user_ibfk_2` FOREIGN KEY (`USER_TYPE`) REFERENCES `user_type` (`TYPE_ID`),
  ADD CONSTRAINT `user_ibfk_3` FOREIGN KEY (`SHIPPING_ADDRESS`) REFERENCES `address` (`ADDRESS_ID`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
