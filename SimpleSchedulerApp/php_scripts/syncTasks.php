<?php
$servername = "localhost";
$username = "root";
$password = "";
$dbname = "ss-db";

//open connection to mysql db
$conn = mysqli_connect($servername,$username,$password,$dbname) or die("Error " . mysqli_error($conn));

//define structure
$task_name=$_POST['t1'];
$category=$_POST['t2'];
$time=$_POST['t3'];
$recur=$_POST['t4'];
$email_notification=$_POST['t5'];
$push_notification=$_POST['t6'];
$completed=$_POST['t7'];

//execute sql statement
$sql = "INSERT INTO task (task_name, category, time, recur, email_notification, push_notification, completed) VALUES ($task_name, $category, $time, $recur, $email_notification, $push_notification, $completed);";
mysqli_query($conn, $sql);
echo "Task insert success";
?>