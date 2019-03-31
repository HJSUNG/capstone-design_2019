<?php
/* Important!!!
    The target DBMS uses MyISAM. It does not support foreign key constraint.
    I thought it supported InnoDB. It supports foreign key.
    We need to consider the foreign key constraints. When sending queries. */

    $host = 'localhost';
    $username = 'capstone02';
    $password = 'capstone02!@';
    $dbname = 'capstone02';

    // $options = array(PDO::MYSQL_ATTR_INIT_COMMAND => 'SET NAMES utf8');

    // Connect to database.
    try {
        $con = new PDO("mysql:host={$host};dbname={$dbname};charset=utf8",$username, $password);
        echo("Connected to database successfully!<br>");
    } catch(PDOException $e) {
        die("Failed to connect to the database: " . $e->getMessage());
    }
    $con->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
    $con->setAttribute(PDO::ATTR_DEFAULT_FETCH_MODE, PDO::FETCH_ASSOC);
    /*

    if(function_exists('get_magic_quotes_gpc') && get_magic_quotes_gpc()) {
        function undo_magic_quotes_gpc(&$array) {
            foreach($array as &$value) {
                if(is_array($value)) {
                    undo_magic_quotes_gpc($value);
                }
                else {
                    $value = stripslashes($value);
                }
            }
        }

        undo_magic_quotes_gpc($_POST);
        undo_magic_quotes_gpc($_GET);
        undo_magic_quotes_gpc($_COOKIE);
    }

    header('Content-Type: text/html; charset=utf-8');
    #session_start();
    */
?>
