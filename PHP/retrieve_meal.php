<?php
// error_reporting(E_ALL);
// ini_set('display_errors',1);

// Connect to the databse
include('dbcon.php');

$android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");


    if( (($_SERVER['REQUEST_METHOD'] == 'POST') && isset($_POST['submit'])) || $android ) {
        try {
            $ID = (int)$_POST['ID'];
            $stmt = $con->prepare('SELECT * FROM Meal WHERE UserID = :ID ORDER BY DateRegistered DESC');
            $stmt->bindParam(':ID', $ID);
            $stmt->execute();
            $stmt->setFetchMode(PDO::FETCH_BOTH);

            //$result=$stmt->fetchAll(PDO::FETCH_ASSOC);

            if( !($stmt->rowCount() > 0)) {
                echo('No Result set!');
            }

            while( $row=$stmt->fetch() ) {
                echo('<comma>' . $row[2] . '<comma>' . $row[3] . '<comma>' . $row[4] . '<comma>' . $row[5] . '<comma>' . $row[6] . '<comma>' . $row[1] . '<br>');
            }
        }
        catch(PDOException $e) {
            die("Retrieve error!" . $e->getMessage());
        }
    }
?>
