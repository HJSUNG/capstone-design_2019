<?php
// https://zetawiki.com/wiki/PHP_%EC%BF%A0%ED%82%A4_%EB%A1%9C%EA%B7%B8%EC%9D%B8_%EA%B5%AC%ED%98%84
// error_reporting(E_ALL);
// ini_set('display_errors',1);

// Connect to the databse
include('dbcon.php');
/*
if(!isset($_COOKIE['user_id']) || !isset($_COOKIE['user_name'])) {
	echo "<meta http-equiv='refresh' content='0;url=login.php'>";
	exit;
}
*/

    $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");


    if( (($_SERVER['REQUEST_METHOD'] == 'POST') && isset($_POST['submit'])) || $android ) {
      $ID=$_POST['ID'];
      $Password=$_POST['Password'];
      $Salt="123";
      $Password_salted=$Password . $Salt;
      $Password_hash=hash("sha256", $Password_salted);

      try {
        $stmt = $con->prepare('SELECT * FROM User WHERE UserName = :ID AND Password = :Password');
        $stmt->bindParam(':ID', $ID);
        $stmt->bindParam(':Password', $Password_hash);
        $stmt->execute();
        //$result=$stmt->fetchAll(PDO::FETCH_ASSOC);

        if($stmt->rowCount() > 0) {
          $row=$stmt->fetch();
          echo("Login success!<br>");
          echo($row['UserID']."<br>");
          $stmt = $con->prepare('SELECT * FROM User WHERE UserID = :UserID');
          $stmt->bindParam(':UserID', $ID);
          $stmt->execute();
          $row = $result->fetch();
          $name = $row['Name'];
          setcookie('UserID', $ID, time()+(86400*30),'/');
          setcookie('Name', $name, time()+(86400*30),'/');
        } else {
          echo("Login Fail!<br>");
        }
      }
      catch(PDOException $e) {
        die("Login error!" . $e->getMessage());
      }
    }

//    echo($Password_hash);
    $user_id = $_COOKIE['user_id'];
    $user_name = $_COOKIE['user_name'];
    echo "<p>안녕하세요. $user_name($user_id)님</p>";
?>