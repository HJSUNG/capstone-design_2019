<?php
/* Important!!!
    The target DBMS uses MyISAM. It does not support foreign key constraint.
    I thought it supported InnoDB. It supports foreign key.
    We need to consider the foreign key constraints. When sending queries. */

    $host = 'localhost';
    $username = 'capstone02';
    $password = 'capstone02!@';
    $dbname = 'capstone02';

    // Connect to database.
    try {
        $con = new PDO("mysql:host={$host};dbname={$dbname};charset=utf8",$username, $password);
        //echo("Connected to database successfully!<br>");
    } catch(PDOException $e) {
        die("Failed to connect to the database: " . $e->getMessage());
    }
    $con->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
    $con->setAttribute(PDO::ATTR_DEFAULT_FETCH_MODE, PDO::FETCH_ASSOC);
?>
