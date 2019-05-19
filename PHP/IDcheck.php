<?php
// https://zetawiki.com/wiki/PHP_%EC%BF%A0%ED%82%A4_%EB%A1%9C%EA%B7%B8%EC%9D%B8_%EA%B5%AC%ED%98%84
// error_reporting(E_ALL);
// ini_set('display_errors',1);

// Connect to the databse
include('dbcon.php');

    $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");


    if( (($_SERVER['REQUEST_METHOD'] == 'POST') && isset($_POST['submit'])) || $android ) {

      $ID=$_POST['ID'];

      try {
        $stmt = $con->prepare('SELECT * FROM User WHERE UserName = :ID');
        $stmt->bindParam(':ID', $ID);
        $stmt->execute();
        //$result=$stmt->fetchAll(PDO::FETCH_ASSOC);

        if($stmt->rowCount() > 0) {
          echo("ID already exists!");
        } else {
          echo("You can use this ID");
        }
      }
      catch(PDOException $e) {
        die("ID check error!" . $e->getMessage());
      }
    }
?>