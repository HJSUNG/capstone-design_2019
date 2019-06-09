<?php
// error_reporting(E_ALL);
// ini_set('display_errors',1);

// Connect to the databse
include('dbcon.php');

$android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");


    if( (($_SERVER['REQUEST_METHOD'] == 'POST') && isset($_POST['submit'])) || $android ) {
        try {
            $ID = (int)$_POST['ID'];
            $stmt = $con->prepare('SELECT * FROM BloodGlucose 
                WHERE (UserID = :ID AND DateRegistered >= ( CURDATE() - INTERVAL 3 DAY ) ) ORDER BY DateRegistered ASC');
            $stmt->bindParam(':ID', $ID);
            $stmt->execute();
            $stmt->setFetchMode(PDO::FETCH_BOTH);

            //$result=$stmt->fetchAll(PDO::FETCH_ASSOC);

            if( !($stmt->rowCount() > 0)) {
                echo('No Result set!');
            }

            while( $row=$stmt->fetch() ) {
                // UserID, DateRegistered, Contents,
                // Value, positive, compound, neutral, negative, DiaryID
                //echo($row['UserID']."<br>");
                // UserID, DateRegiestered, Value, BloodGlucoseID, Comment
                echo($row[0] . '<comma>' . $row[1] . '<comma>' . $row[2] . '<comma>' . '<br>');
            }
        }
        catch(PDOException $e) {
            die("Retrieve error!" . $e->getMessage());
        }
    }
?>
